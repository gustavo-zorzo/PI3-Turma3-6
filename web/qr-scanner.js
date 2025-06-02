// Função para processar o QR code escaneado
async function processQRCode(qrData, credentials = null) {
    try {
        // Converter string do QR code para objeto
        const qrInfo = JSON.parse(qrData);
        console.log('QR code escaneado:', qrInfo);

        // Verificar se o QR code é válido
        if (!qrInfo.sessionId || !qrInfo.type || qrInfo.type !== 'login') {
            throw new Error('QR code inválido');
        }

        // Verificar se o QR code não está expirado
        if (qrInfo.expiresAt && qrInfo.expiresAt < Date.now()) {
            throw new Error('QR code expirado');
        }

        // Referência para a sessão no Firebase
        const sessionRef = firebase.database().ref('qrSessions/' + qrInfo.sessionId);
        
        // Verificar se a sessão existe
        const snapshot = await sessionRef.once('value');
        const sessionData = snapshot.val();

        if (!sessionData) {
            throw new Error('Sessão não encontrada');
        }

        if (sessionData.expiresAt < Date.now()) {
            throw new Error('Sessão expirada');
        }

        // Se credenciais foram fornecidas, tenta fazer login
        if (credentials && credentials.email && credentials.password) {
            try {
                // Tentar fazer login
                const userCredential = await firebase.auth().signInWithEmailAndPassword(
                    credentials.email,
                    credentials.password
                );
                
                // Atualizar sessão com dados de autenticação
                await sessionRef.update({
                    status: 'authenticated',
                    email: credentials.email,
                    uid: userCredential.user.uid,
                    authenticatedAt: Date.now()
                });

                return {
                    success: true,
                    message: 'Login realizado com sucesso',
                    sessionId: qrInfo.sessionId
                };
            } catch (error) {
                throw new Error('Credenciais inválidas: ' + error.message);
            }
        } else {
            // Se não há credenciais, apenas verifica e retorna o status da sessão
            return {
                success: true,
                requiresCredentials: true,
                message: 'QR code válido, aguardando credenciais',
                sessionId: qrInfo.sessionId
            };
        }

    } catch (error) {
        console.error('Erro ao processar QR code:', error);
        return {
            success: false,
            message: error.message || 'Erro ao processar QR code'
        };
    }
}

// Função para verificar status da sessão
async function checkSessionStatus(sessionId) {
    try {
        const sessionRef = firebase.database().ref('qrSessions/' + sessionId);
        const snapshot = await sessionRef.once('value');
        const data = snapshot.val();

        if (!data) {
            return { valid: false, message: 'Sessão não encontrada' };
        }

        if (data.expiresAt < Date.now()) {
            return { valid: false, message: 'Sessão expirada' };
        }

        return { valid: true, data: data };
    } catch (error) {
        console.error('Erro ao verificar status da sessão:', error);
        return { valid: false, message: error.message };
    }
}

// Função para autenticar com credenciais
async function authenticateWithCredentials(sessionId, email, password) {
    try {
        const result = await processQRCode(JSON.stringify({
            sessionId: sessionId,
            type: 'login',
            timestamp: Date.now()
        }), { email, password });

        return result;
    } catch (error) {
        console.error('Erro na autenticação:', error);
        return {
            success: false,
            message: error.message || 'Erro na autenticação'
        };
    }
}

// Exportar funções
window.QRScanner = {
    processQRCode,
    checkSessionStatus,
    authenticateWithCredentials
}; 