import {
  createStore
} from 'vuex';

const store = createStore({
  state: {

    name: null,
    age: null,
    birthdate: null,
    email: null,
    docid: null,
    password: null,
    databaseIdOfUser: null

  },
  mutations: {
    setEmail: (state, email) => {
      state.email = email
    },
    setName: (state, name) => {
      state.name = name;
    },
    setAge: (state, age) => {
      state.age = age;
    },
    setPassword: (state, password) => {
      state.password = password;
    },
    setDocid: (state, id) => {
      state.id = id;
    },
    setBirthdate: (state, birthdate) => {
      state.birthdate = birthdate;
    },
    setDatabaseIdOfUser: (state, databaseIdOfUser) => {
      state.databaseIdOfUser = databaseIdOfUser;
    },
  },
  getters: {
    getEmail: (state) =>
      state.email,
    getName: (state) =>
      state.name,
    getAge: (state) =>
      state.age,
    getPassword: (state) =>
      state.password,
    getDocid: (state) =>
      state.id,
    getBirthdate: (state) =>
      state.birthdate,
    getDatabaseIdOfUser: (state) =>
      state.databaseIdOfUser
  }
})

export default store