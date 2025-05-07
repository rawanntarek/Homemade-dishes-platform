function addSellers(event) {
    event.preventDefault();

    const textarea = document.getElementById('companyNames');
    const companiesinput = textarea.value.trim();
    if (!companiesinput) {
        document.getElementById('message').innerText = 'Please enter at least one company name.';
        return false;
    }

    const names = companiesinput.split('\n').map(name => name.trim()).filter(name => name);

    fetch('http://localhost:8080/admin-srvc-1.0-SNAPSHOT/api/admin/createSellers', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(names)
    })
    .then(response => response.json())
    .then(result => {
        alert( result.join('\n'));
        textarea.value = ''; // clear input
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('message').innerText = 'Failed to add sellers.';
    });

    return false;
}
