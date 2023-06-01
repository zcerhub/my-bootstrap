kubectl create serviceaccount license-server-k8s-sa

kubectl create clusterrolebinding license-server-k8s-sa-admin --clusterrole=cluster-admin --serviceaccount=default:license-server-k8s-sa


kubectl get secret $(kubectl get serviceaccount license-server-k8s-sa -o jsonpath='{.secrets[0].name}') -o jsonpath='{.data.token}' | base64 --decode