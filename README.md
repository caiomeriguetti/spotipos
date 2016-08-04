# Deploy on ubuntu server

## Install Everything

```bash
./install.sh
```

## Load initial data into redis
```python
python load-data.py
```

## Deploy

```bash
./deploy.sh
```

then access http://localhost:8282/api/properties?ax=0&ay=100&bx=100&by=0
