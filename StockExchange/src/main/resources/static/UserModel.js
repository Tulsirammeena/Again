class UserModel {
    constructor(username, firstName, city, email, number, pan, role, password) {
        this.username = username;
        this.firstName = firstName;
        this.city = city;
        this.email = email;
        this.number = number;
        this.pan = pan;
        this.role = role;
        this.password = password;
    }
}

export default UserModel;

// toJSON() {
//     return {
//         userName: this.userName,
//         firstName: this.firstName,
//         lastName: this.lastName,
//         email: this.email,
//         pan: this.pan,
//         number: this.number,
//         role: this.role,
//         password: this.password
//     };
// }