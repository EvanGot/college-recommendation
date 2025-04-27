import request from '../utils/request'

export function listuser(query) {
  return request({
    url: '/system/news/liststatus',
    method: 'get',
    params: query
  })
}

