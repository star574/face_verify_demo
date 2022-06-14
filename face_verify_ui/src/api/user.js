import request from '@/utils/request'

export default {
    register(user) {
        return request({
            url: "/register",
            method: "POST",
            data: user
        })
    },
    verify(user) {
        return request({
            url: "/verify",
            method: "POST",
            data: user
        })
    }
}