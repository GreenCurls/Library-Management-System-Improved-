package com.library.management.classes;
import com.library.management.database.databaseConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class Book{
    private String title;
    private Author author;
    private String ISBN;
    private String publicationDate;
    private int availableCopies;
    int bookId;

    //Constructor
    public Book(String title, Author author, String ISBN, String publicationDate, int availableCopies){
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.publicationDate = publicationDate;
        this.availableCopies = availableCopies;
        this.bookId = insertBookIntoDatabase();
    }

    //Getters
    public String getTitle(){
        return title;
    }

    public Author getAuthor(){
        return author;
    }

    public String getISBN(){
        return ISBN;
    }

    public String getPublicationDate(){
        return publicationDate;
    }

    public int getAvailableCopies(){
        return availableCopies;
    }

    public int getBookId() {
        return bookId;
    }


    //Setter
    public void setTitle(String title){
        this.title = title;
    }

    public void setAuthor(Author author){
        this.author = author;
    }

    public void setISBN(String ISBN){
        this.ISBN = ISBN;
    }

    public void setPublicationDate(String publicationDate){
        this.publicationDate = publicationDate;
    }

    public void setAvailableCopies(int availableCopies){
        this.availableCopies = availableCopies;
        updateBookInDatabase();
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    //Methods
    public void borrowBook(){
        if(availableCopies > 0){
            availableCopies--;
            updateBookInDatabase();
        }else{
            System.out.println("No Available Copies to Borrow");
        }
    }

    public void returnBook(){
        availableCopies++;
        updateBookInDatabase();
    }

    private int insertBookIntoDatabase() {
        try(Connection conn = databaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement
            ("INSERT INTO Books (title, author_id, ISBN, publication_date, available_copies) VALUES (?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, title);
            stmt.setInt(2, author.getAuthorId());
            stmt.setString(3, ISBN);
            stmt.setString(4, publicationDate);
            stmt.setInt(5, availableCopies);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting book: " + e.getMessage());
        }
        return -1;
    }

    private void updateBookInDatabase() {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Books SET title = ?, author_id = ?, ISBN = ?, publication_date = ?, available_copies = ? WHERE book_id = ?")) {
            stmt.setString(1, title);
            stmt.setInt(2, author.getAuthorId());
            stmt.setString(3, ISBN);
            stmt.setString(4, publicationDate);
            stmt.setInt(5, availableCopies);
            stmt.setInt(6, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
        }
    }
}

