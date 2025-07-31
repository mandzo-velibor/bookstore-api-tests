package com.bookstore.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private int id;
    private String title;
    private int pageCount;
    private String description;
    private String excerpt;
    private String publishDate;
}
