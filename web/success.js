sdocument.addEventListener('DOMContentLoaded', () => {
    // Verificar se o usuário está autenticado
    firebase.auth().onAuthStateChanged((user) => {
        if (user) {
            // Atualizar informações do usuário
            updateUserInfo(user);
            
            // Redirecionar após 3 segundos
            setTimeout(() => {
                goToDashboard();
            }, 3000);
        } else {
            // Se não estiver autenticado, voltar para a página de login
            window.location.href = '/index.html';
        }
    });
});

// Atualizar informações do usuário na tela
function updateUserInfo(user) {
    const userName = document.getElementById('userName');
    const userEmail = document.getElementById('userEmail');

    // Buscar dados adicionais do usuário no banco
    firebase.database().ref('users/' + user.uid).once('value')
        .then((snapshot) => {
            const userData = snapshot.val();
            if (userData && userData.name) {
                userName.textContent = userData.name;
            } else {
                userName.textContent = user.email.split('@')[0];
            }
            userEmail.textContent = user.email;
        })
        .catch((error) => {
            console.error('Erro ao buscar dados do usuário:', error);
            userName.textContent = user.email.split('@')[0];
            userEmail.textContent = user.email;
        });
}

// Função para ir para o dashboard
function goToDashboard() {
    window.location.href = '/dashboard.html';
}

// Função para fazer logout
function logout() {
    firebase.auth().signOut()
        .then(() => {
            window.location.href = '/index.html';
        })
        .catch((error) => {
            console.error('Erro ao fazer logout:', error);
        });
} 