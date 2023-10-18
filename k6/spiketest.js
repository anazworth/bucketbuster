import http from "k6/http";
import { sleep } from "k6";

export const options = {
  stages: [
    { duration: "2m", target: 1000 },
    { duration: "2m", target: 5000 },
  ],
};

export default () => {
  const res = http.get("http://localhost:8080/");
  sleep(1);
};
