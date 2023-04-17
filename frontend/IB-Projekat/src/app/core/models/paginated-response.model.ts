export interface PaginatedResponse<T> {
    pageNumber: number,
    size: number,
    content: Array<T>
}