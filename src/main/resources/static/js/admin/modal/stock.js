document.addEventListener("DOMContentLoaded", () => {
    const viewModal = document.getElementById("viewModalOverlay");
    const editModalOverlay = document.getElementById("modalOverlay");
    const openModalBtn = document.getElementById("openModal");

    // Elements View
    const viewId = document.getElementById("viewId");
    const viewProductName = document.getElementById("viewProductName");
    const viewQuantity = document.getElementById("viewQuantity");
    const btnViewEdit = document.getElementById("btnViewEdit");
    const btnViewDelete = document.getElementById("btnViewDelete");

    // Elements Form
    const formId = document.getElementById("stockId");
    const formProduct = document.getElementById("product");
    const formQuantity = document.getElementById("quantity");
    const modalTitle = document.getElementById("modalTitle");

    const closeViewModal = () => { if (viewModal) viewModal.style.display = "none"; };

    if (openModalBtn) {
        openModalBtn.addEventListener("click", () => {
            if (formId) formId.value = "";
            if (formProduct) formProduct.value = "";
            if (formQuantity) formQuantity.value = "";
            if (modalTitle) modalTitle.textContent = "Adicionar Estoque";
            if (editModalOverlay) editModalOverlay.style.display = "flex";
        });
    }

    document.querySelectorAll(".edit").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            if (formId) formId.value = btn.getAttribute("data-id");
            if (formProduct) formProduct.value = btn.getAttribute("data-product");
            if (formQuantity) formQuantity.value = btn.getAttribute("data-quantity");
            if (modalTitle) modalTitle.textContent = "Editar Estoque";
            if (editModalOverlay) editModalOverlay.style.display = "flex";
        });
    });

    document.querySelectorAll(".view").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            if (viewId) viewId.textContent = btn.getAttribute("data-id");
            if (viewProductName) viewProductName.textContent = btn.getAttribute("data-product-name");
            if (viewQuantity) viewQuantity.textContent = btn.getAttribute("data-quantity");
            if (btnViewDelete) btnViewDelete.href = btn.getAttribute("data-delete");

            if (btnViewEdit) {
                btnViewEdit.onclick = () => {
                    closeViewModal();
                    // Simula clique no edit original
                    const originalEdit = document.querySelector(`.edit[data-id='${btn.getAttribute("data-id")}']`);
                    if (originalEdit) originalEdit.click();
                };
            }
            if (viewModal) viewModal.style.display = "flex";
        });
    });
});