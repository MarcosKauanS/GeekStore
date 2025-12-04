document.addEventListener("DOMContentLoaded", () => {
    const viewModal = document.getElementById("viewModalOverlay");
    const editModalOverlay = document.getElementById("modalOverlay");
    const openModalBtn = document.getElementById("openModal");

    // Elementos View
    const viewId = document.getElementById("viewId");
    const viewName = document.getElementById("viewName");
    const viewDescription = document.getElementById("viewDescription"); // NOVO
    const viewPrice = document.getElementById("viewPrice");
    const viewCategory = document.getElementById("viewCategory");
    const viewImage = document.getElementById("viewImage");
    const btnViewEdit = document.getElementById("btnViewEdit");
    const btnViewDelete = document.getElementById("btnViewDelete");

    // Elementos Form
    const formId = document.getElementById("productId");
    const formName = document.getElementById("produto-nome");
    const formDescription = document.getElementById("produto-descricao"); // NOVO
    const formPrice = document.getElementById("produto-preco");
    const formCategory = document.getElementById("produto-categoria");
    const previewImage = document.getElementById("previewImage");
    const productInputFile = document.getElementById("produto-imagem");
    const modalTitle = document.getElementById("modalTitle");

    const closeViewModal = () => { if (viewModal) viewModal.style.display = "none"; };

    // Setup Preview Imagem
    if (productInputFile) {
        productInputFile.addEventListener("change", function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    if (previewImage) {
                        previewImage.src = e.target.result;
                        previewImage.style.display = "inline-block";
                    }
                };
                reader.readAsDataURL(file);
            }
        });
    }

    const setupImagePreview = (imgFile) => {
        if (!previewImage) return;
        if (imgFile && imgFile.trim() !== "" && imgFile !== "null") {
            const path = imgFile.startsWith("/") ? imgFile : "/" + imgFile;
            previewImage.src = path;
            previewImage.style.display = "inline-block";
        } else {
            previewImage.src = "";
            previewImage.style.display = "none";
        }
    };

    if (openModalBtn) {
        openModalBtn.addEventListener("click", () => {
            if (formId) formId.value = "";
            if (formName) formName.value = "";
            if (formDescription) formDescription.value = ""; // LIMPA NOVO CAMPO
            if (formPrice) formPrice.value = "";
            if (formCategory) formCategory.value = "";
            setupImagePreview(null);
            if (productInputFile) productInputFile.value = "";
            if (modalTitle) modalTitle.textContent = "Adicionar Produto";
            if (editModalOverlay) editModalOverlay.style.display = "flex";
        });
    }

    document.querySelectorAll(".edit").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            if (formId) formId.value = btn.getAttribute("data-id");
            if (formName) formName.value = btn.getAttribute("data-name");
            if (formDescription) formDescription.value = btn.getAttribute("data-description"); // PREENCHE NOVO CAMPO
            if (formPrice) formPrice.value = btn.getAttribute("data-price");
            if (formCategory) formCategory.value = btn.getAttribute("data-category");
            if (productInputFile) productInputFile.value = "";
            setupImagePreview(btn.getAttribute("data-image"));
            if (modalTitle) modalTitle.textContent = "Editar Produto";
            if (editModalOverlay) editModalOverlay.style.display = "flex";
        });
    });

    document.querySelectorAll(".view").forEach(btn => {
        btn.addEventListener("click", (event) => {
            event.preventDefault();
            if (viewId) viewId.textContent = btn.getAttribute("data-id");
            if (viewName) viewName.textContent = btn.getAttribute("data-name");
            if (viewDescription) viewDescription.textContent = btn.getAttribute("data-description"); // EXIBE NOVO CAMPO
            if (viewPrice) viewPrice.textContent = "R$ " + parseFloat(btn.getAttribute("data-price")).toFixed(2);
            if (viewCategory) viewCategory.textContent = btn.getAttribute("data-category");

            const imageUrl = btn.getAttribute("data-image");
            if (viewImage) {
                if (imageUrl && imageUrl !== "null" && !imageUrl.includes("/null")) {
                    viewImage.src = imageUrl;
                    viewImage.style.display = "inline-block";
                } else {
                    viewImage.style.display = "none";
                }
            }

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