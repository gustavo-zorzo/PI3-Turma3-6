package br.com.superid

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import androidx.camera.core.ExperimentalGetImage

// Composable que exibe a câmera
@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreviewView(
    lifecycleOwner: LifecycleOwner,
    onTokenDetected: (String) -> Unit
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    val barcodeScanner = remember {
        BarcodeScanning.getClient()
    }
    // Adiciona o PreviewView ao layout Compose
    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Cria preview da câmera
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Seleciona câmera traseira
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Configura análise da imagem para detectar QR Code
            val analysis = ImageAnalysis.Builder().build().also { imageAnalysis ->
                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy: ImageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val inputImage = InputImage.fromMediaImage(
                            mediaImage, imageProxy.imageInfo.rotationDegrees
                        )

                        // Processa a imagem procurando QR Code
                        barcodeScanner.process(inputImage)
                            .addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {
                                    barcode.rawValue?.let { token ->
                                        imageAnalysis.clearAnalyzer()
                                        onTokenDetected(token)
                                    }
                                }
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    } else {
                        imageProxy.close()
                    }
                }
            }

            // Liga câmera com preview e análise de QR
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, analysis
                )
            } catch (e: Exception) {
                Log.e("CameraScreen", "Erro ao iniciar preview da câmera", e)
            }

        }, ContextCompat.getMainExecutor(context))
    }
}

// Composable principal da tela da câmera
@Composable
fun CameraScreenContent(lifecycleOwner: LifecycleOwner) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    // Launcher para solicitar permissão de uso da câmera
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted = granted
        if (!granted) {
            Toast.makeText(context, "Permissão da câmera negada", Toast.LENGTH_SHORT).show()
        }
    }

    // Solicita permissão ao carregar composable
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // Se permissão concedida, mostra preview e overlay de scanner
    if (permissionGranted) {
        CameraPreviewView(lifecycleOwner = lifecycleOwner) { token ->
            handleLoginToken(token, context)
        }

        QRScannerOverlay(
            onBack = {
                val intent = Intent(context, DashboardActivity::class.java)
                context.startActivity(intent)
                (context as? ComponentActivity)?.finish()
            }
        )
    }
}

// Atividade que inicia a tela de câmera com verificação de e-mail
class CameraScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && !user.isEmailVerified) {
            Toast.makeText(
                this,
                "Valide o seu e-mail antes de acessar esta área.",
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }

        setContent {
            CameraScreenContent(this)
        }
    }
}

// Sobreposição gráfica com borda de leitura e botão de voltar
@Composable
fun QRScannerOverlay(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0x99000000))
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.Center)
                .border(2.dp, Color.White)
                .background(Color.Transparent)
        )
        Text(
            text = "Aponte a câmera para o QR Code",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
        )
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
        }
    }
}

// Função que trata o token lido, associando o login ao usuário autenticado no Firebase
fun handleLoginToken(token: String, context: android.content.Context) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser

    if (user == null) {
        Toast.makeText(context, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
        return
    }

    db.collection("login")
        .whereEqualTo("loginToken", token)
        .limit(1)
        .get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val docRef = documents.documents[0].reference
                docRef.update(
                    mapOf(
                        "user" to user.uid,
                        "userEmail" to user.email,
                        "loginTime" to Timestamp.now(),
                        "status" to "completed"
                    )
                ).addOnSuccessListener {
                    Toast.makeText(context, "Login sem senha concluído!", Toast.LENGTH_LONG).show()
                    context.startActivity(Intent(context, DashboardActivity::class.java))
                    (context as? ComponentActivity)?.finish()
                }.addOnFailureListener {
                    Toast.makeText(context, "Erro ao salvar login", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Token inválido ou expirado", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Erro ao buscar login", Toast.LENGTH_SHORT).show()
        }
}