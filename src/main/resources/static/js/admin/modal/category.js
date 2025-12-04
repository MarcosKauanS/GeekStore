document.addEventListener("DOMContentLoaded", () => {
    // --- Elementos Modais ---
    const viewModal = document.getElementById("viewModalOverlay");
    const editModalOverlay = document.getElementById("modalOverlay");
    const openModalBtn = document.getElementById("openModal");

    // --- Elementos Visualização ---
    const viewId = document.getElementById("viewId");
    const viewName = document.getElementById("viewName");
    const btnViewEdit = document.getElementById("btnViewEdit");
    const btnViewDelete = document.getElementById("btnViewDelete");

    // --- Elementos Formulário ---
    const formId = document.getElementById("categoryId");
    const formName = document.getElementById("categoria-nome");
    const modalTitle = document.getElementById("modalTitle");

    // Helper fechar
    const closeViewModal = () => { if (viewModal) viewModal.style.display = "none"; };

    // 1. Adicionar
    if (openModalBtn) {
        openModalBtn.addEventListener("click", () => {
            if (formId) formId.value = "";
            if (formName) formName.value = "";
            if (modalTitle) modalTitle.textContent = "Adicionar Categoria";
            if (editModalOverlay) editModalOverlay.style.display = "flex";
        });
    }

    // 2. Editar (Desktop)
    document.querySelectorAll(".edit").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            // Padronizando uso de getAttribute para consistência
            if (formId) formId.value = btn.getAttribute("data-id");
            if (formName) formName.value = btn.getAttribute("data-name");
            if (modalTitle) modalTitle.textContent = "Editar Categoria";
            if (editModalOverlay) editModalOverlay.style.display = "flex";
        });
    });

    // 3. Ver Mais / Editar (Mobile)
    document.querySelectorAll(".view").forEach(btn => {
        btn.addEventListener("click", (event) => {
            event.preventDefault();

            const id = btn.getAttribute("data-id");
            const name = btn.getAttribute("data-name");
            const deleteUrl = btn.getAttribute("data-delete");

            // Preenche Visualização com verificação
            if (viewId) viewId.textContent = id;
            if (viewName) viewName.textContent = name;
            if (btnViewDelete) btnViewDelete.href = deleteUrl;

            // Configura botão Editar do Modal de Visualização
            if (btnViewEdit) {
                btnViewEdit.onclick = () => {
                    closeViewModal();

                    // Simula o clique no botão de editar original (se existir) 
                    // OU preenche manualmente se preferir a lógica explícita:
                    if (formId) formId.value = (id && id !== "undefined") ? id : "";
                    if (formName) formName.value = name;
                    if (modalTitle) modalTitle.textContent = "Editar Categoria";
                    if (editModalOverlay) editModalOverlay.style.display = "flex";
                };
            }

            if (viewModal) viewModal.style.display = "flex";
        });
    });
});