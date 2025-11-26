document.addEventListener("DOMContentLoaded", () => {
    // Elementos
    const viewModal = document.getElementById("viewModalOverlay");
    const editModalOverlay = document.getElementById("modalOverlay");
    
    // Elementos Visualização
    const viewId = document.getElementById("viewId");
    const viewName = document.getElementById("viewName");
    const btnViewEdit = document.getElementById("btnViewEdit");
    const btnViewDelete = document.getElementById("btnViewDelete");

    // Elementos Formulário
    const formId = document.getElementById("categoryId");
    const formName = document.getElementById("categoria-nome");
    const modalTitle = document.getElementById("modalTitle");

    const openModalBtn = document.getElementById("openModal");

    // Fechar Modal Auxiliar
    const closeViewModal = () => viewModal.style.display = "none";

    // 1. Adicionar
    if(openModalBtn) {
        openModalBtn.addEventListener("click", () => {
            formId.value = "";
            formName.value = "";
            modalTitle.textContent = "Adicionar Categoria";
            editModalOverlay.style.display = "flex";
        });
    }

    // 2. Editar (Desktop)
    document.querySelectorAll(".edit").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            const id = btn.dataset.id; // Dataset é seguro aqui pois vem do thymeleaf
            
            formId.value = id || "";
            formName.value = btn.dataset.name;
            modalTitle.textContent = "Editar Categoria";
            editModalOverlay.style.display = "flex";
        });
    });

    // 3. Ver Mais / Editar (Mobile)
    document.querySelectorAll(".view").forEach(btn => {
        btn.addEventListener("click", (event) => {
            event.preventDefault();
            
            const id = btn.getAttribute("data-id");
            const name = btn.getAttribute("data-name");
            const deleteUrl = btn.getAttribute("data-delete");

            // Preenche Visualização
            viewId.textContent = id;
            viewName.textContent = name;
            btnViewDelete.href = deleteUrl;

            // Configura botão Editar do Modal
            btnViewEdit.onclick = () => {
                closeViewModal();
                
                formId.value = (id && id !== "undefined") ? id : "";
                formName.value = name;
                modalTitle.textContent = "Editar Categoria";
                editModalOverlay.style.display = "flex";
            };
            
            viewModal.style.display = "flex";
        });
    });
});