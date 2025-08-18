export type Student = {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    courseIdList: number[];
    courseNameList: string[];
}

export const BLANK_STUDENT: Student = {
    id: 0, 
    firstName: "", 
    lastName: "", 
    email: "", 
    phone: "", 
    courseIdList: [], 
    courseNameList: []
}