export class SignUpRequest {

   email: string;
   fullName: string;
   password: string;
   matchingPassword: string;

   construct() {
   }
   
   passwordsMatch(): boolean {
      return this.password === this.matchingPassword;
   }

}
