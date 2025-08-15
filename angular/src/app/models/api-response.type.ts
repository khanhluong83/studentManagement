export type ApiResponse<T> = {
    success: string;
    error: string;
    entity: T;
}