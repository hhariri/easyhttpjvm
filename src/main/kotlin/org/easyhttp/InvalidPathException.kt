package org.easyHttp

public class InvalidPathException(path: String): Exception("Path $path not found") {
}