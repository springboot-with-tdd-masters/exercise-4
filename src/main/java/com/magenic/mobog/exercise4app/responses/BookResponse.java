package com.magenic.mobog.exercise4app.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

    private Long id;
    private Long authorId;
    private String title;
    private String createdAt;
    private String updatedAt;
}
