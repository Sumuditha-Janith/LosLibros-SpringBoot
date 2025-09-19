package lk.ijse.gdse71.loslibros.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long bookId;
    private String bookTitle;
    private String bookDescription;
    private Double bookPrice;
    private Integer bookQuantity;
    private String bookImage;
    private AuthorDTO bookAuthor;
    private CategoryDTO bookCategory;
}