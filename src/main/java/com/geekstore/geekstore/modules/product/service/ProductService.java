package com.geekstore.geekstore.modules.product.service;

import com.geekstore.geekstore.modules.product.model.Product;
import com.geekstore.geekstore.modules.product.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Diretório onde as imagens serão salvas (dentro do static/)
    public static final String ENDERECO_ARMAZENAMENTO_ARQUIVO = "src/main/resources/static/images/";

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ---------------------------------------------------------
    // SALVAR IMAGEM — COM TODOS OS CONTROLES, SEM OMISSÕES
    // ---------------------------------------------------------
    public String saveImageUrl(MultipartFile arquivo) throws IOException {

        if (arquivo == null || arquivo.isEmpty()) {
            return null; // ou exception dependendo da regra de negócio
        }

        // Cria o diretório caso ainda não exista
        Path diretorio = Paths.get(ENDERECO_ARMAZENAMENTO_ARQUIVO);
        if (!Files.exists(diretorio)) {
            Files.createDirectories(diretorio);
        }

        // Caminho completo do destino
        Path destino = diretorio.resolve(arquivo.getOriginalFilename()).normalize();

        // Proteção contra Path Traversal (../../etc/passwd)
        Path diretorioPermitido = diretorio.toAbsolutePath().normalize();
        Path destinoAbsoluto = destino.toAbsolutePath().normalize();
        if (!destinoAbsoluto.startsWith(diretorioPermitido)) {
            throw new SecurityException("Nome do arquivo não é viável!");
        }

        // ============================================
        // CÓPIA DA IMAGEM — maneira CORRETA no Windows
        // Usando try-with-resources para garantir fechamento do Stream
        // ============================================
        try (InputStream inputStream = arquivo.getInputStream()) {

            // Deleta o arquivo antigo (se houver)
            Files.deleteIfExists(destino);

            // Copia a nova imagem
            Files.copy(
                    inputStream,
                    destino,
                    StandardCopyOption.REPLACE_EXISTING);
        }

        // Caminho que o navegador pode acessar (dentro de static/)
        return "images/" + arquivo.getOriginalFilename();
    }

    // ---------------------------------------------------------
    // MÉTODOS CRUD ↓
    // ---------------------------------------------------------
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> search(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(keyword);
        }
        return productRepository.findAll();
    }
}