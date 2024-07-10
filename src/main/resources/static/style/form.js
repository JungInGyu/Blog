document.addEventListener('DOMContentLoaded', function() {
    const signupBtn = document.getElementById("sign-up");
    const signinBtn = document.getElementById("sign-in");
    const loginForm = document.getElementById("login-in");
    const signupForm = document.getElementById("login-up");

    signupBtn.addEventListener("click", () => {
        loginForm.classList.add("none");
        signupForm.classList.remove("none");
        signupForm.classList.add("block");
    });

    signinBtn.addEventListener("click", () => {
        signupForm.classList.add("none");
        loginForm.classList.remove("none");
        loginForm.classList.add("block");
    });
});