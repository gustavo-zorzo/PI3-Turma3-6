// Initialize Firebase
const firebaseConfig = {
    apiKey: "AIzaSyAIGxo8pxOntTuBTZPMrH5IHN48kP8b_ps",
    authDomain: "superid-fe402.firebaseapp.com",
    projectId: "superid-fe402",
    storageBucket: "superid-fe402.appspot.com",
    messagingSenderId: "758906893412",
    appId: "1:758906893412:android:336e549d0361c805e61b71",
    databaseURL: "https://superid-fe402-default-rtdb.firebaseio.com"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

// Initialize Firebase Auth and Firestore
const auth = firebase.auth();
const db = firebase.firestore();

// Set up authentication state observer
auth.onAuthStateChanged((user) => {
    if (user) {
        console.log('Usuário está logado:', user.uid);
        // Criar ou atualizar informações do usuário no Firestore
        db.collection('users').doc(user.uid).set({
            lastLogin: firebase.firestore.Timestamp.now(),
            email: user.email
        }, { merge: true });
    } else {
        console.log('Usuário não está logado');
    }
}); 