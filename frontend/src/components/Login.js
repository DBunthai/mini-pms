import React from "react";
import { Button, Form, Modal } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "react-query";
import { accessTokenKey, api, refreshTokenKey } from "../libs/api";
import { useDispatch } from "react-redux";
import { login } from "../features/authSlice";
const Login = ({ show, handleClose }) => {
  const LoginSchema = z.object({
    email: z.string().email(),
    password: z.string().min(3).max(20),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({ resolver: zodResolver(LoginSchema) });
  const dispatch = useDispatch();

  const loginMutation = useMutation(async (data) => {
    api.post("/auth/token", data).then((res) => {
      dispatch(
        login({
          accessToken: res.data.accessToken,
          refreshToken: res.data.refreshToken,
        })
      );
      localStorage.setItem(accessTokenKey, res.data.accessToken);
      localStorage.setItem(refreshTokenKey, res.data.refreshToken);
      handleClose();
    });
  });

  const onSubmit = (data) => {
    loginMutation.mutate(data);
  };
  return (
    <div>
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Login</Modal.Title>
        </Modal.Header>
        <Modal.Body className="p-4">
          <Form onSubmit={handleSubmit(onSubmit)}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
              <Form.Label>Email address</Form.Label>
              <Form.Control {...register("email")} placeholder="Enter email" />
              <Form.Text className="text-danger">
                {errors.email?.message}
              </Form.Text>
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control {...register("password")} placeholder="Password" />
              <Form.Text className="text-danger">
                {errors.password?.message}
              </Form.Text>
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicCheckbox">
              <Form.Check type="checkbox" label="Remember me" />
            </Form.Group>
            <Button variant="primary" type="submit" style={{ width: "100%" }}>
              Login
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default Login;