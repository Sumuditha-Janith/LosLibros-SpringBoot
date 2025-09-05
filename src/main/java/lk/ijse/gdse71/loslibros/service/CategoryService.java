package lk.ijse.gdse71.loslibros.service;

import lk.ijse.gdse71.loslibros.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO saveCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Long id);
    void deleteCategory(Long id);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
}