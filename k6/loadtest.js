import http from "k6/http";
import { sleep } from "k6";

export const options = {
  stages: [
    { duration: "1m", target: 1000 },
    { duration: "1m", target: 2000 },
    { duration: "1m", target: 3000 },
    { duration: "1m", target: 4000 },
    { duration: "1m", target: 5000 },
    { duration: "1m", target: 6000 },
    { duration: "1m", target: 7000 },
    { duration: "1m", target: 8000 },
    { duration: "1m", target: 9000 },
    { duration: "1m", target: 10000 },
    { duration: "1m", target: 11000 },
    { duration: "1m", target: 12000 },
    { duration: "1m", target: 13000 },
    { duration: "1m", target: 14000 },
    { duration: "1m", target: 15000 },
  ],
};

export default () => {
  const res = http.get("http://localhost:8080/");
  sleep(1);
};
