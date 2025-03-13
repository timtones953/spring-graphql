package com.example.graphql.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@RequestMapping("${base.path}/v1")
public abstract class BaseController {
}
