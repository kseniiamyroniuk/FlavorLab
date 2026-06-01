package model;

public class Review {

    private User author;
    private int rating;     // 1–5
    private String comment;

    public Review(User author, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be 1–5");
        }
        this.author = author;
        this.rating = rating;
        this.comment = comment;
    }

    public User getAuthor() {
        return author;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}