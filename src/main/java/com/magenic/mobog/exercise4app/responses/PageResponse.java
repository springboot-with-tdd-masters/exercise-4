package com.magenic.mobog.exercise4app.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/** Map only necessary data needed by client.
 *
 * @param <T>
 */
@Builder
@Data
public class PageResponse<T> {
    List<T> content;
    Integer page;
    Integer size;
    Integer totalPage;
    Long totalElement;
}
