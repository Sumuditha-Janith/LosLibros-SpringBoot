package lk.ijse.gdse71.loslibros.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false)
    private String bookTitle;

    @Column(columnDefinition = "TEXT")
    private String bookDescription;

    @Column(nullable = false)
    private Double bookPrice;

    @Column(nullable = false)
    private Integer bookQuantity;

    private String bookImage;

    @ManyToOne
    @JoinColumn(name = "bookAuthor", nullable = false)
    private Author bookAuthor;

    @ManyToOne
    @JoinColumn(name = "bookCategory", nullable = false)
    private Category bookCategory;
}