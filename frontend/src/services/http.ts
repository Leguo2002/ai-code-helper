import axios, { AxiosInstance } from 'axios';

export function createAxios(): AxiosInstance {
  const instance = axios.create({
    baseURL: '/api',
    timeout: 30000
  });

  instance.interceptors.response.use(
    (resp) => resp,
    (error) => {
      return Promise.reject(error);
    }
  );

  return instance;
}


