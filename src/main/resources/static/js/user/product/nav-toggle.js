document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.querySelector(".nav-toggle");
    const menu = document.querySelector("nav ul");
    toggle.addEventListener("click", () => menu.classList.toggle("active"));
});