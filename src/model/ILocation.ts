import {User} from "./IUser";

export interface Location {
    id: number;
    city: string;
    country: string;
    tip: string;
    rating: number;
    user: User;
}