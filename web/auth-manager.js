// Função para criar novo usuário
async function createUser(email, password) {
    try {
        const userCredential = await auth.createUserWithEmailAndPassword(email, password);
        const user = userCredential.user;
        
        // Criar perfil do usuário no banco
        await database.ref('users/' + user.uid).set({
            email: email,
            createdAt: Date.now(),
            lastLogin: Date.now()
        });

        return user;
    } catch (error) {
        console.error('Erro ao criar usuário:', error);
        throw error;
    }
}

// Função para fazer login com email/senha
async function loginWithEmail(email, password) {
    try {
        const userCredential = await auth.signInWithEmailAndPassword(email, password);
        return userCredential.user;
    } catch (error) {
        console.error('Erro ao fazer login:', error);
        throw error;
    }
}

// Função para fazer logout
async function logout() {
    try {
        await auth.signOut();
    } catch (error) {
        console.error('Erro ao fazer logout:', error);
        throw error;
    }
}

// Função para verificar sessão QR
async function checkQRSession(sessionId) {
    try {
        const sessionRef = database.ref('qrSessions/' + sessionId);
        const snapshot = await sessionRef.once('value');
        return snapshot.val();
    } catch (error) {
        console.error('Erro ao verificar sessão:', error);
        throw error;
    }
}

// Função para atualizar perfil do usuário
async function updateUserProfile(userId, data) {
    try {
        await database.ref('users/' + userId).update(data);
    } catch (error) {
        console.error('Erro ao atualizar perfil:', error);
        throw error;
    }
}

// Função para obter dados do usuário
async function getUserData(userId) {
    try {
        const snapshot = await database.ref('users/' + userId).once('value');
        return snapshot.val();
    } catch (error) {
        console.error('Erro ao obter dados do usuário:', error);
        throw error;
    }
}

// Função para criar sessão QR
async function createQRSession(userId) {
    try {
        const sessionId = generateSessionId();
        const sessionRef = database.ref('qrSessions/' + sessionId);
        
        await sessionRef.set({
            userId: userId,
            created: Date.now(),
            status: 'pending'
        });

        return sessionId;
    } catch (error) {
        console.error('Erro ao criar sessão QR:', error);
        throw error;
    }
}

// Função para gerar ID de sessão único
function generateSessionId() {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
}

// Função para autenticar via QR code
async function authenticateWithQR(email, password, uid) {
    try {
        // Fazer login com email e senha
        const userCredential = await auth.signInWithEmailAndPassword(email, password);
        const user = userCredential.user;

        // Verificar se o UID corresponde
        if (user.uid !== uid) {
            throw new Error('UID não corresponde ao usuário');
        }

        // Atualizar dados do usuário
        await updateUserProfile(user.uid, {
            lastLogin: Date.now(),
            lastLoginMethod: 'qr_code',
            email: email
        });

        return user;
    } catch (error) {
        console.error('Erro na autenticação QR:', error);
        throw error;
    }
}

// Função para validar sessão QR
async function validateQRSession(sessionId, email, password, uid) {
    try {
        const sessionRef = database.ref('qrSessions/' + sessionId);
        
        // Verificar se a sessão existe
        const snapshot = await sessionRef.once('value');
        const sessionData = snapshot.val();

        if (!sessionData) {
            throw new Error('Sessão não encontrada');
        }

        if (Date.now() - sessionData.created > 5 * 60 * 1000) {
            throw new Error('Sessão expirada');
        }

        // Atualizar sessão com dados de autenticação
        await sessionRef.update({
            status: 'authenticated',
            email: email,
            password: password, // Nota: Em produção, considere criptografar a senha
            uid: uid,
            authenticatedAt: Date.now()
        });

        return true;
    } catch (error) {
        console.error('Erro ao validar sessão QR:', error);
        throw error;
    }
}

// Função para verificar status da autenticação
function checkAuthStatus(callback) {
    return auth.onAuthStateChanged(callback);
}

// Função para obter usuário atual
function getCurrentUser() {
    return auth.currentUser;
}

// Função para verificar se o email já existe
async function checkEmailExists(email) {
    try {
        const methods = await auth.fetchSignInMethodsForEmail(email);
        return methods.length > 0;
    } catch (error) {
        console.error('Erro ao verificar email:', error);
        throw error;
    }
}

// Função para gerar token persistente para QR code
async function generatePersistentQRToken(userId) {
    try {
        const token = generateSecureToken();
        await database.ref('userTokens/' + userId).set({
            qrToken: token,
            createdAt: Date.now(),
            lastUsed: Date.now()
        });
        return token;
    } catch (error) {
        console.error('Erro ao gerar token:', error);
        throw error;
    }
}

// Função para validar token persistente
async function validatePersistentQRToken(userId, token) {
    try {
        const tokenRef = database.ref('userTokens/' + userId);
        const snapshot = await tokenRef.once('value');
        const tokenData = snapshot.val();

        if (!tokenData || tokenData.qrToken !== token) {
            return false;
        }

        // Atualizar último uso do token
        await tokenRef.update({
            lastUsed: Date.now()
        });

        return true;
    } catch (error) {
        console.error('Erro ao validar token:', error);
        return false;
    }
}

// Função para gerar token seguro
function generateSecureToken() {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let token = '';
    for (let i = 0; i < 32; i++) {
        token += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return token;
} 