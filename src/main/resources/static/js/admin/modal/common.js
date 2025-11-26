/* ==================================== */
/* ⚙️ JS Comum - Modal e Sidebar        */
/* ==================================== */

document.addEventListener("DOMContentLoaded", () => {
    // --- Lógica da Sidebar ---
    const sidebar = document.querySelector('.sidebar');
    
    // Cria o botão toggle dinamicamente se não existir (ou pode ser fixo no HTML)
    if (sidebar && !document.querySelector('.sidebar-toggle')) {
        const toggleBtn = document.createElement('button');
        toggleBtn.innerHTML = '☰';
        toggleBtn.classList.add('sidebar-toggle');
        document.body.appendChild(toggleBtn);

        toggleBtn.addEventListener('click', () => {
            sidebar.classList.toggle('open');
        });
    }

    // --- Lógica Básica de Fechar Modais (Global) ---
    const modals = document.querySelectorAll('.modal-overlay');
    const closeButtons = document.querySelectorAll('.close, .btn-cancel');

    const closeModal = (modal) => {
        modal.style.display = "none";
    };

    closeButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            modals.forEach(m => closeModal(m));
        });
    });

    modals.forEach(modal => {
        modal.addEventListener("click", (e) => {
            if (e.target === modal) closeModal(modal);
        });
    });
});