kubectl delete -f orders-service.yaml
kubectl delete -f orders-deployment.yaml
kubectl delete -f postgres-service.yaml
kubectl delete -f postgres-deployment.yaml
kubectl delete -f postgres-pvc.yaml
kubectl delete -f secret.yaml
kubectl delete -f configmap.yaml
