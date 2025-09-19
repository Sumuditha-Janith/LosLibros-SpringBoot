package lk.ijse.gdse71.loslibros.controller;

import lk.ijse.gdse71.loslibros.dto.BookDTO;
import lk.ijse.gdse71.loslibros.dto.PurchaseRequest;
import lk.ijse.gdse71.loslibros.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("save")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        BookDTO savedBook = bookService.saveBook(bookDTO);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping("getAllBooks")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','USER')")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("get/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','USER')")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/purchase")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','USER')")
    public ResponseEntity<String> purchaseBooks(@RequestBody List<PurchaseRequest> purchaseRequests) {
        bookService.processPurchase(purchaseRequests);
        return ResponseEntity.ok("Purchase successful. Stock updated.");
    }
}
