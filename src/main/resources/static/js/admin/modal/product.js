document.addEventListener("DOMContentLoaded", () => {
    const viewModal = document.getElementById("viewModalOverlay");
    const editModalOverlay = document.getElementById("modalOverlay");

    // View Elements
    const viewId = document.getElementById("viewId");
    const viewName = document.getElementById("viewName");
    const viewPrice = document.getElementById("viewPrice");
    const viewCategory = document.getElementById("viewCategory");
    const viewImage = document.getElementById("viewImage");
    const btnViewEdit = document.getElementById("btnViewEdit");
    const btnViewDelete = document.getElementById("btnViewDelete");

    // Form Elements
    const formId = document.getElementById("productId");
    const formName = document.getElementById("produto-nome");
    const formPrice = document.getElementById("produto-preco");
    const formCategory = document.getElementById("produto-categoria");
    const previewImage = document.getElementById("previewImage");
    const productInputFile = document.getElementById("produto-imagem"); // O Input File
    const modalTitle = document.getElementById("modalTitle");
    const openModalBtn = document.getElementById("openModal");

    const closeViewModal = () => viewModal.style.display = "none";

    // --- NOVA LÓGICA: PREVIEW AO SELECIONAR ARQUIVO ---
    if (productInputFile) {
        productInputFile.addEventListener("change", function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    previewImage.src = e.target.result;
                    previewImage.style.display = "inline-block";
                };
                reader.readAsDataURL(file);
            } else {
                // Se o usuário cancelar a seleção e não tiver imagem prévia
                if (!formId.value) {
                    previewImage.src = "";
                    previewImage.style.display = "none";
                }
            }
        });
    }

    // Helper para Preview de Imagem vinda do Backend
    const setupImagePreview = (imgFile) => {
        if (imgFile && imgFile.trim() !== "" && imgFile !== "null") {
            const path = imgFile.startsWith("/") ? imgFile : "/" + imgFile;
            previewImage.src = path;
            previewImage.style.display = "inline-block";
        } else {
            previewImage.src = "";
            previewImage.style.display = "none";
        }
    };

    // 1. Botão ADICIONAR
    if (openModalBtn) {
        openModalBtn.addEventListener("click", () => {
            formId.value = "";
            formName.value = "";
            formPrice.value = "";
            formCategory.value = "";

            // Limpa imagem e input file
            setupImagePreview(null);
            if (productInputFile) productInputFile.value = "";

            modalTitle.textContent = "Adicionar Produto";
            editModalOverlay.style.display = "flex";
        });
    }

    // 2. Botões EDITAR (da Tabela)
    document.querySelectorAll(".edit").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            const id = btn.getAttribute("data-id");

            formId.value = id || "";
            formName.value = btn.getAttribute("data-name");
            formPrice.value = btn.getAttribute("data-price");
            formCategory.value = btn.getAttribute("data-category");

            // Limpa o input de arquivo (mantém a foto antiga no backend se vazio)
            if (productInputFile) productInputFile.value = "";

            setupImagePreview(btn.getAttribute("data-image"));

            modalTitle.textContent = "Editar Produto";
            editModalOverlay.style.display = "flex";
        });
    });

    // 3. Botões VER MAIS (Mobile)
    document.querySelectorAll(".view").forEach(btn => {
        btn.addEventListener("click", (event) => {
            event.preventDefault();

            const id = btn.getAttribute("data-id");
            const name = btn.getAttribute("data-name");
            const price = btn.getAttribute("data-price");
            const categoryName = btn.getAttribute("data-category");
            const categoryId = btn.getAttribute("data-cat-id");
            const imageUrl = btn.getAttribute("data-image");
            const rawImage = btn.getAttribute("data-raw-image");

            viewId.textContent = id;
            viewName.textContent = name;
            viewPrice.textContent = "R$ " + parseFloat(price).toFixed(2);
            viewCategory.textContent = categoryName;

            if (imageUrl && imageUrl !== "null" && !imageUrl.includes("/null")) {
                viewImage.src = imageUrl;
                viewImage.style.display = "inline-block";
            } else {
                viewImage.style.display = "none";
            }

            btnViewDelete.href = btn.getAttribute("data-delete");

            btnViewEdit.onclick = () => {
                closeViewModal();

                formId.value = (id && id !== "undefined") ? id : "";
                formName.value = name;
                formPrice.value = price;
                formCategory.value = categoryId;

                if (productInputFile) productInputFile.value = "";

                setupImagePreview(rawImage);

                modalTitle.textContent = "Editar Produto";
                editModalOverlay.style.display = "flex";
            };

            viewModal.style.display = "flex";
        });
    });
});