import { Role } from './Role';

export class User {

  email: string;
  firstName: string;
  lastName: string;
  provider: string;
  imageUrl: string;
  created: Date;
  lastLogin: Date;
  roles: Role[];

}
