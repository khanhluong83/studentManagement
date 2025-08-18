export type Course = {
    id: number;
    code: string;
    name: string;
    startDate: string;
    endDate: string;
}

export const BLANK_COURSE: Course = {
    id: 0, 
    code: "", 
    name: "", 
    startDate: "", 
    endDate: ""
}