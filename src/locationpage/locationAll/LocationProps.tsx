import {User} from "../../model/IUser";

export interface LocationProps {
  id?: number;
  city?: string;
  country?: string;
  tip?: string;
  rating?: number;
  user?: User;
}
