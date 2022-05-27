package com.magenic.mobog.exercise4app.adapters;

import com.magenic.mobog.exercise4app.entities.Author;
import com.magenic.mobog.exercise4app.requests.AddAuthorRequest;
import com.magenic.mobog.exercise4app.responses.AuthorResponse;
import com.magenic.mobog.exercise4app.responses.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorAdapter {
    public AuthorResponse mapAuthorToResponse(Author author) {
        return AuthorResponse
                .builder()
                .id(author.getId())
                .name(author.getName())
                .updatedAt(author.getLastModifiedDate().toString())
                .createdAt(author.getCreatedDate().toString())
                .build();
    }
    public PageResponse<AuthorResponse> mapPageToPageResponse(Page<Author> page){
        List<AuthorResponse> authorResponseList = page.get().map(this::mapAuthorToResponse).collect(Collectors.toList());
        Pageable pageReq = page.getPageable();
        return PageResponse.<AuthorResponse>builder()
                .size(page.getSize())
                .page(pageReq.getPageNumber())
                .size(pageReq.getPageSize())
                .content(authorResponseList)
                .totalPage(page.getTotalPages())
                .totalElement(page.getTotalElements())
                .build();
    }
    public Author mapRequestToAuthorEntity(AddAuthorRequest request){
        Author author = new Author();
        author.setName(request.getName());
        return author;
    }
}
