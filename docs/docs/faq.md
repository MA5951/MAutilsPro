# FAQ

**Q: How do I change the default current limit?**
A: Override in `PowerSystemConstants`:
```java
new PowerSystemConstants().withStatorCurrentLimit(true, 30);
```

**Q: Can I use another controller type?**
A: Implement `MAController` and register it via `DefaultRobotContainer.setDriverController(...)`.
