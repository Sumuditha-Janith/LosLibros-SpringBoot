package lk.ijse.gdse71.loslibros.service.impl;

import lk.ijse.gdse71.loslibros.dto.CategoryDTO;
import lk.ijse.gdse71.loslibros.entity.Category;
import lk.ijse.gdse71.loslibros.repository.CategoryRepository;
import lk.ijse.gdse71.loslibros.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        CategoryDTO dto = modelMapper.map(savedCategory, CategoryDTO.class);
        // Set book count
        dto.setBookCount(savedCategory.getBooks() != null ? savedCategory.getBooks().size() : 0);
        return dto;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    CategoryDTO dto = modelMapper.map(category, CategoryDTO.class);
                    // Set book count
                    dto.setBookCount(category.getBooks() != null ? category.getBooks().size() : 0);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        CategoryDTO dto = modelMapper.map(category, CategoryDTO.class);
        // Set book count
        dto.setBookCount(category.getBooks() != null ? category.getBooks().size() : 0);
        return dto;
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        existingCategory.setCategoryName(categoryDTO.getCategoryName());

        Category updatedCategory = categoryRepository.save(existingCategory);
        CategoryDTO dto = modelMapper.map(updatedCategory, CategoryDTO.class);
        // Set book count
        dto.setBookCount(updatedCategory.getBooks() != null ? updatedCategory.getBooks().size() : 0);
        return dto;
    }
}