// Função para atualizar o status do login
async function updateLoginStatus(loginToken, status, userData = null) {
    try {
        const db = firebase.firestore();
        const loginRef = db.collection('login').doc(loginToken);
        
        const updateData = {
            status: status,
            updatedAt: firebase.firestore.Timestamp.now()
        };

        if (status === 'completed' && userData) {
            updateData.user = userData.uid;
            updateData.completedAt = firebase.firestore.Timestamp.now();
        }

        await loginRef.update(updateData);
        return true;
    } catch (error) {
        console.error('Erro ao atualizar status:', error);
        return false;
    }
}

// Função para verificar se o token é válido
async function validateLoginToken(loginToken) {
    try {
        const db = firebase.firestore();
        const doc = await db.collection('login').doc(loginToken).get();

        if (!doc.exists) {
            return { valid: false, message: 'Token não encontrado' };
        }

        const data = doc.data();
        
        if (data.expiresAt.toMillis() < Date.now()) {
            await doc.ref.update({ status: 'expired' });
            return { valid: false, message: 'Token expirado' };
        }

        if (data.status !== 'pending') {
            return { valid: false, message: 'Token já utilizado' };
        }

        return { valid: true, data: data };
    } catch (error) {
        console.error('Erro ao validar token:', error);
        return { valid: false, message: 'Erro ao validar token' };
    }
}

// Função para completar o processo de login
async function completeLogin(loginToken, user) {
    try {
        const validation = await validateLoginToken(loginToken);
        
        if (!validation.valid) {
            return { success: false, message: validation.message };
        }

        // Atualizar status para completed
        await updateLoginStatus(loginToken, 'completed', user);

        return { 
            success: true, 
            message: 'Login completado com sucesso'
        };
    } catch (error) {
        console.error('Erro ao completar login:', error);
        return { 
            success: false, 
            message: 'Erro ao completar login'
        };
    }
}

// Exportar funções
window.LoginManager = {
    updateLoginStatus,
    validateLoginToken,
    completeLogin
}; 