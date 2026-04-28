# Menambahkan semua perubahan
git add .

# Melakukan commit dengan pesan berisi waktu saat ini
$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
git commit -m "Update: $timestamp"

# Push ke branch main di GitHub
git push origin main
