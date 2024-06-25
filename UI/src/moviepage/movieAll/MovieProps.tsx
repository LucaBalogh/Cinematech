import {User} from "../../model/IUser";

export interface MovieProps {
  id?: number;
  name?: string;
  tip?: string;
  rating?: number;
  user?: User;
}
