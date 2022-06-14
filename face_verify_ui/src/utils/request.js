import axios from 'axios'

const http = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 6000
})


export default http