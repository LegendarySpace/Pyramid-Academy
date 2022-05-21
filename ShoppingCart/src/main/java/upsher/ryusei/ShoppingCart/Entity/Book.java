package upsher.ryusei.ShoppingCart.Entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class Book extends Product{
    private String genre;
    private String author;
    private String publisher;

    public Book() {
    }

    public Book(String name, double price, String genre, String author, String publisher) {
        super(name, price);
        this.genre = genre;
        this.author = author;
        this.publisher = publisher;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
