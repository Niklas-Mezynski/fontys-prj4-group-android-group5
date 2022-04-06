import {v4 as uuidv4} from 'uuid';

export class Helpers {
    static generateUUID(): string {
        return uuidv4();
    }
}  

