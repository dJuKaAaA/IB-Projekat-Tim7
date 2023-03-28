package ib.projekat.IBprojekat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginatedResponseDto<T> {

    private int pageNumber;
    private int size;
    private Collection<T> content;

}
