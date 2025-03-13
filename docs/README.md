query ($document: String!) {
user(document: $document) {
results {
name
}
}
}

{
"document": "Andre"
}