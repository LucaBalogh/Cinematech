import {User} from "./IUser";

export interface Movie {
    id: number;
    name: string;
    tip: string;
    rating: number;
    user: User;
}