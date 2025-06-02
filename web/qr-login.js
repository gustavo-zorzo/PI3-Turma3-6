document.addEventListener('DOMContentLoaded', () => {
    const qrButton = document.getElementById('qrButton');
    const qrCodeContainer = document.getElementById('qrCodeContainer');
    const qrCodeElement = document.getElementById('qrCode');
    const qrStatus = document.getElementById('qrStatus');
    let qrcode = null;

    function showStatus(message, type = 'info') {
        qrStatus.textContent = message;
        qrStatus.className = 'qr-status ' + type;
        console.log('Status:', message);
    }

    function clearStatus() {
        qrStatus.textContent = '';
        qrStatus.className = 'qr-status';
    }

    function generateLoginToken() {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let token = '';
        for (let i = 0; i < 20; i++) {
            token += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        return token;
    }

    qrButton.addEventListener('click', async () => {
        try {
            qrCodeContainer.classList.toggle('active');
            
            if (qrCodeContainer.classList.contains('active')) {
                qrCodeElement.innerHTML = '';
                showStatus('Gerando QR Code...', 'info');
                
                const loginToken = generateLoginToken();
                console.log('Token gerado:', loginToken);

                const db = firebase.firestore();
                
                // Criar documento no Firestore
                const loginDoc = {
                    loginToken: loginToken,
                    createdAt: firebase.firestore.Timestamp.now(),
                    expiresAt: firebase.firestore.Timestamp.fromMillis(Date.now() + (5 * 60 * 1000)),
                    status: 'pending'
                };

                // Salvar no Firestore
                await db.collection('login').doc(loginToken).set(loginDoc);
                console.log('Documento criado no Firestore');

                // Gerar QR code
                qrcode = new QRCode(qrCodeElement, {
                    text: loginToken,
                    width: 200,
                    height: 200,
                    colorDark: "#000000",
                    colorLight: "#ffffff",
                    correctLevel: QRCode.CorrectLevel.H
                });

                showStatus('QR Code pronto para ser escaneado', 'success');

                // Monitorar documento no Firestore
                const unsubscribe = db.collection('login')
                    .doc(loginToken)
                    .onSnapshot((doc) => {
                        if (!doc.exists) {
                            console.log('Documento não encontrado');
                            return;
                        }

                        const data = doc.data();
                        console.log('Dados atualizados:', data);

                        if (data.status === 'completed' && data.user && data.userEmail) {
                            console.log('Status completed detectado com dados do usuário:', {
                                uid: data.user,
                                email: data.userEmail
                            });
                            
                            // Mostrar mensagem de sucesso
                            showStatus('QR Code lido com sucesso! Redirecionando...', 'success');
                            
                            // Limpar o QR code
                            qrCodeElement.innerHTML = '';
                            if (qrcode) {
                                qrcode.clear();
                            }

                            // Adicionar classe de sucesso ao container
                            qrCodeContainer.classList.add('success');
                            
                            // Parar de monitorar
                            unsubscribe();
                            
                            // Redirecionar após um pequeno delay para mostrar a mensagem
                            setTimeout(() => {
                                window.location.href = 'login-success.html';
                            }, 1500); // 1.5 segundos de delay
                        }
                    }, (error) => {
                        console.error('Erro no listener:', error);
                        showStatus('Erro ao monitorar status', 'error');
                    });

                // Limpar listener após 5 minutos
                setTimeout(() => {
                    unsubscribe();
                    showStatus('Sessão expirada', 'error');
                }, 5 * 60 * 1000);

            } else {
                clearStatus();
                qrCodeElement.innerHTML = '';
                if (qrcode) {
                    qrcode.clear();
                }
            }
        } catch (error) {
            console.error('Erro:', error);
            showStatus('Erro: ' + error.message, 'error');
        }
    });
}); 